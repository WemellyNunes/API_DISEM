package com.disem.API.services;

import com.disem.API.enums.OrdersServices.StatusEnum;
import com.disem.API.models.*;
import com.disem.API.repositories.*;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
public class ReportService {

    @Autowired
    OrderServiceRepository orderService;

    @Autowired
    OrderServiceService orderServiceService;

    @Autowired
    OrderServiceRepository orderServiceRepository;

    @Autowired
    ProgramingRepository programingRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    FinalizeRepository dispatchOSRepository;

    @Autowired
    private FinalizeRepository finalizeRepository;

    public OrderServiceModel findOrderServiceById(Long id) {
        return orderServiceService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ordem de serviço não encontrada"));
    }

    public byte[] generateReport(Long id) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(byteArrayOutputStream);
            Document document = new Document(new PdfDocument(writer));

            InputStream imageStream = getClass().getResourceAsStream("/image/img.png");
            if (imageStream != null) {
                ImageData logoData = ImageDataFactory.create(imageStream.readAllBytes());
                Image logo = new Image(logoData);
                logo.setWidth(UnitValue.createPercentValue(20));
                logo.setHorizontalAlignment(HorizontalAlignment.CENTER);
                document.add(logo);
            } else {
                System.out.println("Imagem não encontrada");
            }

            PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont normalFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            Paragraph universityName = new Paragraph("Universidade Federal do Sul e Sudeste do Pará")
                    .setFont(boldFont)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(universityName);

            Paragraph secretaryName = new Paragraph("Secretaria de Infraestrutura")
                    .setFont(boldFont)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(secretaryName);

            Paragraph divisionName = new Paragraph("Divisão de Serviços de Engenharia e Manutenção")
                    .setFont(boldFont)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(divisionName);

            document.add(new Paragraph("\n"));

            OrderServiceModel os = orderServiceService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Ordem de serviço não encontrada"));

            String tituloRelatorio;
            if (os.getStatus() == StatusEnum.EM_ATENDIMENTO) {
                tituloRelatorio = "Programação n°" + os.getId();
            } else {
                tituloRelatorio = "Relatório n°" + os.getId();
            }

            Paragraph title = new Paragraph(tituloRelatorio)
                    .setFont(boldFont)
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            Paragraph order = new Paragraph("Ordem de serviço")
                    .setFont(boldFont)
                    .setFontSize(13);
            document.add(order);

            LineSeparator ls = new LineSeparator(new SolidLine());
            ls.setWidth(UnitValue.createPercentValue(100));
            ls.setHorizontalAlignment(HorizontalAlignment.CENTER);
            document.add(ls);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            document.add(new Paragraph("N° da requisição: " + os.getRequisition()));
            document.add(new Paragraph("Origem: " + os.getOrigin()));
            document.add(new Paragraph("Solicitante: " + os.getRequester()));
            document.add(new Paragraph("Unidade do solicitante: " + os.getUnit()));
            document.add(new Paragraph("Objeto de preparo: " + os.getPreparationObject()));
            document.add(new Paragraph("Tipo de manutenção: " + os.getTypeMaintenance()));
            document.add(new Paragraph("Sistema: " + os.getSystem()));
            document.add(new Paragraph("Unidade da manutenção: " + os.getMaintenanceUnit()));
            document.add(new Paragraph("Campus: " + os.getCampus()));
            document.add(new Paragraph("Data do registro: " + os.getDate().format(formatter)));

            document.add(new Paragraph("\n"));

            ProgramingModel activePrograming = programingRepository.findByOrderServiceIdAndActive(id, "true");
            if (activePrograming != null) {
                // Adiciona as informações da programação ativa no relatório
                Paragraph prog = new Paragraph("Programação")
                        .setFont(boldFont)
                        .setFontSize(13);
                document.add(prog);

                LineSeparator l1 = new LineSeparator(new SolidLine());
                l1.setWidth(UnitValue.createPercentValue(100));
                l1.setHorizontalAlignment(HorizontalAlignment.CENTER);
                document.add(l1);

                document.add(new Paragraph("Data programada: " + activePrograming.getDatePrograming().format(formatter)));
                document.add(new Paragraph("Horario programado: " + activePrograming.getTime()));
                document.add(new Paragraph("Encarregado: " + activePrograming.getOverseer()));
                document.add(new Paragraph("Profissionais: " + activePrograming.getWorker()));
                document.add(new Paragraph("Observação: " + activePrograming.getObservation()));
                document.add(new Paragraph("Data do registro: " + activePrograming.getCreationDate().format(formatter)));

                document.add(new Paragraph("\n"));

                List<ImageModel> imageModels = imageRepository.findByProgramingId(activePrograming.getId());

                if (!imageModels.isEmpty()) {
                    Paragraph image = new Paragraph("Memorial fotográfico")
                            .setFont(boldFont)
                            .setFontSize(13);
                    document.add(image);

                    LineSeparator l3 = new LineSeparator(new SolidLine());
                    l3.setWidth(UnitValue.createPercentValue(100));
                    l3.setHorizontalAlignment(HorizontalAlignment.CENTER);
                    document.add(l3);

                    document.add(new Paragraph("\n"));

                    // Log para verificação do caminho da imagem
                    System.out.println("Imagens encontradas, processando...");

                    for (ImageModel imageModel : imageModels) {
                        String imagePath = System.getProperty("user.dir") + imageModel.getNameFile();

                        // Verificar o caminho da imagem
                        System.out.println("Caminho da imagem: " + imagePath);

                        try {
                            ImageData imageData = ImageDataFactory.create(imagePath);
                            Image pdfImage = new Image(imageData);

                            pdfImage.setWidth(UnitValue.createPercentValue(60));
                            document.add(pdfImage);
                        } catch (IOException e) {
                            document.add(new Paragraph("Falha ao carregar a imagem: " + imageModel.getNameFile()));
                        }

                        document.add(new Paragraph("Descrição: " + imageModel.getDescription()));
                        document.add(new Paragraph("\n"));
                    }

                } else {
                    System.out.println("Nenhuma imagem encontrada.");
                }

                if (os.getStatus() == StatusEnum.FINALIZADO) {
                    List<FinalizeModel> dispatches = finalizeRepository.findByProgramingId(activePrograming.getId());

                    if (!dispatches.isEmpty()) {
                        Paragraph dispatchTitle = new Paragraph("Finalização")
                                .setFont(boldFont)
                                .setFontSize(13);
                        document.add(dispatchTitle);

                        LineSeparator separator = new LineSeparator(new SolidLine());
                        separator.setWidth(UnitValue.createPercentValue(100));
                        separator.setHorizontalAlignment(HorizontalAlignment.CENTER);
                        document.add(separator);

                        for (FinalizeModel dispatch : dispatches) {
                            document.add(new Paragraph("Observação final: " + dispatch.getContent()));
                            document.add(new Paragraph("Data do registro: " + dispatch.getDateContent().format(formatter)));
                        }
                    }
                }
            } else {
                document.add(new Paragraph("Nenhuma programação ativa encontrada para esta ordem de serviço."));
            }

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }


}
