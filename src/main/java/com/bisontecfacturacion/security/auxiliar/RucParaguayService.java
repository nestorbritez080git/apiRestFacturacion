package com.bisontecfacturacion.security.auxiliar;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RucParaguayService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String API_URL = "https://rucparaguay.info/api/contribuyente/";
    private final String TOKEN = "8d241849af1418656556061c2e9fbc72ba40efba6113d4487913891694ef2838";

    public String consultarContribuyente(String ruc) {
        String url = API_URL + ruc;

        // Cabeceras
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + TOKEN);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                return "Error: " + response.getStatusCode();
            }
        } catch (Exception e) {
            return "Error en la petición: " + e.getMessage();
        }
    }
}
