package com.disem.API.services;

import com.disem.API.models.OrderServiceModel;
import com.disem.API.models.ProgramingModel;
import com.disem.API.repositories.OrderServiceRepository;
import com.disem.API.repositories.ProgramingRepository;
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


import java.io.InputStream;
import java.util.List;
import java.util.UUID;

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

    public byte[] generateReport(UUID id) {
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

            Paragraph title = new Paragraph("Relatório n° " + os.getId())
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

            document.add(new Paragraph("N° da requisição: " + os.getRequisition()));
            document.add(new Paragraph("Unidade do solicitante: " + os.getUnit()));
            document.add(new Paragraph("Solicitante: " + os.getRequester()));
            document.add(new Paragraph("Contato: " + os.getContact()));
            document.add(new Paragraph("Objeto de preparo: " + os.getPreparationObject()));
            document.add(new Paragraph("Tipo de manutenção: " + os.getTypeMaintenance()));
            document.add(new Paragraph("Sistema: " + os.getSystem()));
            document.add(new Paragraph("Unidade da manutenção: " + os.getMaintenanceUnit()));
            document.add(new Paragraph("Campus: " + os.getCampus()));
            document.add(new Paragraph("Observação: " + os.getObservation()));
            document.add(new Paragraph("Data de criação: " + os.getDate()));

            document.add(new Paragraph("\n"));

            List<ProgramingModel> programings = programingRepository.findByOrderServiceId(id);
            for (ProgramingModel programing : programings) {

                Paragraph prog = new Paragraph("Programação")
                        .setFont(boldFont)
                        .setFontSize(13);
                document.add(prog);

                LineSeparator l1 = new LineSeparator(new SolidLine());
                l1.setWidth(UnitValue.createPercentValue(100));
                l1.setHorizontalAlignment(HorizontalAlignment.CENTER);
                document.add(l1);

                document.add(new Paragraph("Id: " + programing.getId()));
                document.add(new Paragraph("Data programada: " + programing.getDatePrograming()));
                document.add(new Paragraph("Horario programado: " + programing.getTime()));
                document.add(new Paragraph("Encarregado: " + programing.getOverseer()));
                document.add(new Paragraph("Funcionarios: " + programing.getWorker()));
                document.add(new Paragraph("Cústo estimado: " + programing.getCost()));
                document.add(new Paragraph("Observação: " + programing.getObservation()));
                document.add(new Paragraph("Data de criação: " + programing.getCreationDate()));
            }

            document.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();

    }
}
