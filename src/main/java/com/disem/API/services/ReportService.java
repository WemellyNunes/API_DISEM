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
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ReportService {

    @Autowired
    private StorageService storageService;

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
            document.add(new Paragraph("Campus: " + os.getCampus().getName().toUpperCase()));
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

                String dataProgramada = "Data programada: " + activePrograming.getStartDate().format(formatter).toUpperCase();
                if (activePrograming.getEndDate() != null) {
                    dataProgramada += " a " + activePrograming.getEndDate().format(formatter).toUpperCase();
                }
                document.add(new Paragraph(dataProgramada));
                document.add(new Paragraph("Horario programado: " + activePrograming.getTime()));
                document.add(new Paragraph("Encarregado: " + activePrograming.getOverseer().toUpperCase()));
                document.add(new Paragraph("Profissionais: " + activePrograming.getWorker()));
                document.add(new Paragraph("Observação: " + activePrograming.getObservation()));
                document.add(new Paragraph("Data do registro: " + activePrograming.getCreationDate().format(formatter)));

                document.add(new Paragraph("\n"));

                if (activePrograming != null) {
                    List<ImageModel> imageModels = imageRepository.findByProgramingId(activePrograming.getId());

                    if (!imageModels.isEmpty()) {
                        document.add(new Paragraph("Memorial Fotográfico").setFont(boldFont).setFontSize(13));

                        LineSeparator separator = new LineSeparator(new SolidLine());
                        document.add(separator);

                        List<ImageModel> antesImages = imageModels.stream()
                                .filter(img -> img.getType() == TypeEnum.antes)
                                .collect(Collectors.toList());

                        List<ImageModel> depoisImages = imageModels.stream()
                                .filter(img -> img.getType() == TypeEnum.depois)
                                .collect(Collectors.toList());

                        adicionarImagensAoRelatorio(document, antesImages, "1. Imagens antes da manutenção");
                        adicionarImagensAoRelatorio(document, depoisImages, "2. Imagens depois da manutenção");
                    }
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
                            document.add(new Paragraph("Observação: " + dispatch.getContent()));
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

    private void adicionarImagensAoRelatorio(Document document, List<ImageModel> imagens, String titulo) {
        if (!imagens.isEmpty()) {
            document.add(new Paragraph(titulo).setFontSize(11).setPaddingBottom(2));

            String firstDescription = imagens.get(0).getDescription();
            LocalDateTime firstImageDate = imagens.get(0).getCreatedAt();

            Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();

            for (int i = 0; i < imagens.size(); i++) {
                ImageModel imageModel = imagens.get(i);
                String imageUrl = imageModel.getNameFile();

                byte[] imageBytes = storageService.downloadFile(imageUrl); // 🔹 Baixa a imagem do MinIO
                if (imageBytes != null) {
                    ImageData imageData = ImageDataFactory.create(imageBytes);
                    Image pdfImage = new Image(imageData);
                    pdfImage.scaleToFit(260, 260);

                    Cell imageCell = new Cell().add(pdfImage).setBorder(null)
                            .setTextAlignment(TextAlignment.CENTER)
                            .setPaddingBottom(2);
                    table.addCell(imageCell);
                } else {
                    table.addCell(new Cell().add(new Paragraph("Erro ao carregar imagem").setFontSize(10)).setBorder(null));
                }

                if ((i + 1) % 2 == 0 || i == imagens.size() - 1) {
                    document.add(table);
                    table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
                    table.setKeepTogether(false);
                }
            }
            document.add(new Paragraph("Descrição: " + firstDescription).setFontSize(10));
            document.add(new Paragraph("Data do registro: " + firstImageDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).setFontSize(10));
            document.add(new Paragraph("\n"));
        }
    }
}
