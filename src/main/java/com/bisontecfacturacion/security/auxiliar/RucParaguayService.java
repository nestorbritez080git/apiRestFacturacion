package com.bisontecfacturacion.security.auxiliar;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class RucParaguayService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String API_URL =
        "https://rucparaguay.info/api/contribuyente/";

    private static final String TOKEN =
        "8d241849af1418656556061c2e9fbc72ba40efba6113d4487913891694ef2838";

    public ResponseEntity<?> consultarContribuyente(String ruc) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0");
        headers.set("Accept", "application/json");
        headers.set("Authorization", "Bearer " + TOKEN);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Object> response = restTemplate.exchange(
                API_URL + ruc,
                HttpMethod.GET,
                entity,
                Object.class   // 🔥 CLAVE
            );

            return ResponseEntity
                    .status(response.getStatusCode())
                    .body(response.getBody());

        } catch (HttpClientErrorException e) {

            Map<String, Object> error = new HashMap<>();
            error.put("message", "Error consultando RUC");
            error.put("status", e.getStatusCode().value());
            error.put("detalle", e.getResponseBodyAsString());

            return ResponseEntity.status(e.getStatusCode())
                    .body(error);
        }
    }
}
