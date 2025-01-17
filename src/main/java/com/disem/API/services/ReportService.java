package com.disem.API.services;

import com.disem.API.enums.OrdersServices.StatusEnum;
import com.disem.API.enums.OrdersServices.TypeEnum;
import com.disem.API.models.*;
import com.disem.API.repositories.*;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
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
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;

import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ReportService {

    @Autowired
    OrderServiceService orderServiceService;

    @Autowired
    ProgramingRepository programingRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    NegationRepository negationRepository;

    @Autowired
    private FinalizeRepository finalizeRepository;

    Color colorGray = new DeviceRgb(128,128,128);

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

            Paragraph universityName = new Paragraph("Universidade Federal do Sul e Sudeste do Pará")
                    .setFont(boldFont)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(colorGray);
            document.add(universityName);

            Paragraph secretaryName = new Paragraph("Secretaria de Infraestrutura")
                    .setFont(boldFont)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(colorGray);
            document.add(secretaryName);

            Paragraph divisionName = new Paragraph("Divisão de Serviços de Engenharia e Manutenção")
                    .setFont(boldFont)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(colorGray);
            document.add(divisionName);

            document.add(new Paragraph("\n"));

            OrderServiceModel os = orderServiceService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Ordem de serviço não encontrada"));

            String tituloRelatorio;
            if (os.getStatus() == StatusEnum.EM_ATENDIMENTO) {
                tituloRelatorio = "Programação da Manutenção n°" + os.getId();
            } else {
                tituloRelatorio = "Relatório da Manutenção n°" + os.getId();
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

            document.add(new Paragraph("N° da requisição: " + os.getRequisition()).setPaddingTop(3));
            document.add(new Paragraph("Origem: " + os.getOrigin()));
            document.add(new Paragraph("Solicitante: " + os.getRequester().toUpperCase()));
            document.add(new Paragraph("Unidade do solicitante: " + os.getUnit().toUpperCase()));
            document.add(new Paragraph("Objeto de preparo: " + os.getPreparationObject().toUpperCase()));
            document.add(new Paragraph("Tipo de manutenção: " + os.getTypeMaintenance()));
            document.add(new Paragraph("Sistema: " + os.getSystem()));
            document.add(new Paragraph("Unidade da manutenção: " + os.getMaintenanceUnit()));
            document.add(new Paragraph("Campus: " + os.getCampus().getName()));
            document.add(new Paragraph("Data do registro: " + os.getDate().format(formatter)));

            document.add(new Paragraph("\n"));

            if (os.getStatus() == StatusEnum.NEGADA) {
                NegationModel negation = negationRepository.findByOrderServiceId(os.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Negação não encontrada para a ordem de serviço."));

                Paragraph negationSectionTitle = new Paragraph("Não aprovada")
                        .setFont(boldFont)
                        .setFontSize(13);
                document.add(negationSectionTitle);

                LineSeparator negationSeparator = new LineSeparator(new SolidLine());
                negationSeparator.setWidth(UnitValue.createPercentValue(100));
                negationSeparator.setHorizontalAlignment(HorizontalAlignment.CENTER);
                document.add(negationSeparator);

                document.add(new Paragraph("Justificativa: " + negation.getContent()));
                document.add(new Paragraph("Data do registro: " + negation.getDate().format(formatter)));
                document.add(new Paragraph("\n"));
            }

            ProgramingModel activePrograming = programingRepository.findByOrderServiceIdAndActive(id, "true");
            if (activePrograming != null) {
                Paragraph prog = new Paragraph("Programação")
                        .setFont(boldFont)
                        .setFontSize(13);
                document.add(prog);

                LineSeparator l1 = new LineSeparator(new SolidLine());
                l1.setWidth(UnitValue.createPercentValue(100));
                l1.setHorizontalAlignment(HorizontalAlignment.CENTER);
                document.add(l1);

                document.add(new Paragraph("Data programada: " + activePrograming.getDatePrograming().format(formatter).toUpperCase()));
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

                    List<ImageModel> antesImages = imageModels.stream()
                            .filter(img -> img.getType() == TypeEnum.antes)
                            .collect(Collectors.toList());

                    List<ImageModel> depoisImages = imageModels.stream()
                            .filter(img -> img.getType() == TypeEnum.depois)
                            .collect(Collectors.toList());

                    if (!antesImages.isEmpty()) {
                        Paragraph antesTitle = new Paragraph("1. Imagens antes da manutenção")
                                .setFontSize(12)
                                .setPaddingBottom(3);
                        document.add(antesTitle);

                        String firstDescription = antesImages.get(0).getDescription();

                        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();

                        for (int i = 0; i < antesImages.size(); i++) {
                            ImageModel imageModel = antesImages.get(i);
                            String imagePath = System.getProperty("user.dir") + imageModel.getNameFile();
                            try {
                                ImageData imageData = ImageDataFactory.create(imagePath);
                                Image pdfImage = new Image(imageData);
                                pdfImage.scaleToFit(300, 300);

                                Cell imageCell = new Cell().add(pdfImage).setBorder(null)
                                        .setTextAlignment(TextAlignment.CENTER)
                                        .setPaddingBottom(3);
                                table.addCell(imageCell);

                            } catch (IOException e) {
                                table.addCell(new Cell().add(new Paragraph("Erro ao carregar imagem").setFontSize(10)).setBorder(null));
                            }

                            if ((i + 1) % 2 == 0 || i == antesImages.size() - 1) {
                                document.add(table);
                                table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
                                table.setKeepTogether(false);
                            }
                        }

                        document.add(new Paragraph("Descrição da(s) imagem(ns): " + firstDescription).setFontSize(10));
                        document.add(new Paragraph("\n"));
                    }

                    if (!depoisImages.isEmpty()) {
                        Paragraph depoisTitle = new Paragraph("2. Imagens depois da manutenção")
                                .setFontSize(12)
                                .setPaddingBottom(3);
                        document.add(depoisTitle);

                        String firstDescription = antesImages.get(0).getDescription();

                        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();

                        for (int i = 0; i < depoisImages.size(); i++) {
                            ImageModel imageModel = depoisImages.get(i);
                            String imagePath = System.getProperty("user.dir") + imageModel.getNameFile();
                            try {
                                ImageData imageData = ImageDataFactory.create(imagePath);
                                Image pdfImage = new Image(imageData);
                                pdfImage.scaleToFit(300,300);

                                Cell imageCell = new Cell().add(pdfImage).setBorder(null)
                                        .setTextAlignment(TextAlignment.CENTER)
                                        .setPaddingBottom(3);
                                table.addCell(imageCell);


                            } catch (IOException e) {
                                table.addCell(new Cell().add(new Paragraph("Erro ao carregar imagem").setFontSize(10)).setBorder(null));
                            }

                            if ((i + 1) % 2 == 0 || i == depoisImages.size() - 1) {
                                document.add(table);
                                table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
                                table.setKeepTogether(false); // Permitir quebra de página na próxima tabela
                            }
                        }

                        document.add(new Paragraph("Descrição da(s) imagem(ns): " + firstDescription).setFontSize(10));
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
                document.add(new Paragraph("Nenhuma programação ativa encontrada para esta ordem de serviço.").setFontSize(10).setFontColor(colorGray).setItalic());
            }

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }
}
