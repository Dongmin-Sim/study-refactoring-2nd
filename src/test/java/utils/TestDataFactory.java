package utils;

import chapter1.data.Invoice;
import chapter1.data.Play;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class TestDataFactory {
    ObjectMapper objectMapper = new ObjectMapper();
    ClassLoader classLoader = getClass().getClassLoader();

    public List<Invoice> createInvoices() {
        List<Invoice> invoices = null;
        try (InputStream inputStream = classLoader.getResourceAsStream("invoices.json")) {
            if (inputStream == null) {
                throw new IllegalArgumentException("file not found");
            }
            invoices = objectMapper.readValue(inputStream, new TypeReference<>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return invoices;
    }

    public Map<String, Play> createPlays() {
        Map<String, Play> plays = null;

        try (InputStream inputStream = classLoader.getResourceAsStream("plays.json")) {
            if (inputStream == null) {
                throw new IllegalArgumentException("file not found");
            }
            plays = objectMapper.readValue(inputStream, new TypeReference<>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return plays;
    }
}
