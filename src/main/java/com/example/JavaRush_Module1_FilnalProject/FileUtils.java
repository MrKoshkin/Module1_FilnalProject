package com.example.JavaRush_Module1_FilnalProject;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class FileUtils {
    private static Path inputPath;
    private static Path outputPath;
    private static String fileExtension;
    private static StringBuilder sb;

    public static Path getInputPath() {
        return inputPath;
    }

    public static Path getOutputPath() {
        return outputPath;
    }

    public static String getFileExtension() {
        return fileExtension;
    }

    public static StringBuilder getSb() {
        return sb;
    }

    public static StringBuilder read(String pathString) throws UnsupportedFileException {
        inputPath = Path.of(pathString);
        sb = new StringBuilder();
        // Определение расширения файла
        if (inputPath.toFile().getName().endsWith(".docx")) {
            readDocx();
        } else if (inputPath.toFile().getName().endsWith(".txt")) {
            readTxt();
        } else {
            throw new UnsupportedFileException("Unsupported file type: " + inputPath.toFile().getName());
        }
        return sb;
    }

    // Чтение docx и запись в StringBuilder
    private static void readDocx() {
        try (XWPFDocument doc = new XWPFDocument(new FileInputStream(inputPath.toFile()))) {
            // Чтение текста из параграфов документа и запись в StringBuilder
            for (XWPFParagraph p : doc.getParagraphs()) {
                sb.append(p.getText()).append("\n");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Чтение txt и запись в StringBuilder
    private static void readTxt() {
        try (Reader reader = new InputStreamReader(new FileInputStream(inputPath.toFile()), StandardCharsets.UTF_8)) {
            int i = -1;
            while ((i = reader.read()) != -1) {
                sb.append((char) i);      //Запись всего текст в Стрингбилдер
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void write(StringBuilder sb) {
        FileUtils.sb = sb;
        File inputFile = new File(inputPath.toUri());
        if (inputPath.toFile().getName().endsWith(".docx")) {
            outputPath = (new File(inputFile.getParent(), "output.docx")).toPath(); // Указываем путь к новому файлу формата docx
            writeDocx();
        } else if (inputPath.toFile().getName().endsWith(".txt")) {
            outputPath = (new File(inputFile.getParent(), "output.txt")).toPath(); // Указываем путь к новому файлу формата txt
            writeTxt();
        }
    }

    // Запись StringBuilder в docx
    private static void writeDocx() {
        try (XWPFDocument newDoc = new XWPFDocument();
             FileOutputStream out = new FileOutputStream(outputPath.toFile())){
            XWPFParagraph p = newDoc.createParagraph();
            p.createRun().setText(sb.toString());
            newDoc.write(out);
        } catch (IOException e) {
        System.out.println("Ошибка при обработке файлов: " + e.getMessage());
        }
    }

    // Запись StringBuilder в txt
    private static void writeTxt() {
        try (FileWriter writer = new FileWriter(outputPath.toFile())) {
            writer.write(sb.toString());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
