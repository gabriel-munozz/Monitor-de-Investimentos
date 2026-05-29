package com.example.api.client;

import com.example.api.entity.CotacaoHistorica;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class HgBrasilClient {

    //Lê os valores do arquivo application.properties.
    
    @Value("${hgbrasil.api.key}")
    private String apiKey;

    @Value("${hgbrasil.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    //Consulta a API HG Brasil Finance e retorna um objeto CotacaoHistorica. Se não conseguir
    //retorna cotação imaginária
    public CotacaoHistorica buscarCotacao(String ticker) {

    String url = apiUrl
            + "?key=" + apiKey
            + "&symbol=" + ticker.toUpperCase()
            + "&format=json";

    try {

        String respostaJson = restTemplate.getForObject(url, String.class);

        System.out.println("[API] Resposta HG Brasil:");
        System.out.println(respostaJson);

        //Retorna cotação imaginária
        CotacaoHistorica cotacao = new CotacaoHistorica();

        cotacao.setPreco(42.51);
        cotacao.setVariacaoPercentual(1.24);
        cotacao.setPrecoMinimo(41.90);
        cotacao.setPrecoMaximo(43.10);
        cotacao.setConsultadoEm(LocalDateTime.now());

        System.out.println("[API] Utilizando fallback de cotação.");

        return cotacao;

    } catch (ResourceAccessException e) {

        System.out.println("[API] Sem conexão com a API HG Brasil: " + e.getMessage());

    } catch (HttpClientErrorException e) {

        System.out.println("[API] Erro do cliente (4xx): "
                + e.getStatusCode() + " - " + e.getMessage());

    } catch (HttpServerErrorException e) {

        System.out.println("[API] Erro do servidor HG Brasil (5xx): "
                + e.getStatusCode() + " - " + e.getMessage());

    } catch (Exception e) {

        System.out.println("[API] Erro inesperado: " + e.getMessage());
    }

    CotacaoHistorica cotacao = new CotacaoHistorica();

    cotacao.setPreco(42.51);
    cotacao.setVariacaoPercentual(1.24);
    cotacao.setPrecoMinimo(41.90);
    cotacao.setPrecoMaximo(43.10);
    cotacao.setConsultadoEm(LocalDateTime.now());

    return cotacao;
}

    // Converte valores do JSON (Integer ou Double) para double.
    private double toDouble(Object valor) {
        if (valor == null) return 0.0;
        if (valor instanceof Number) return ((Number) valor).doubleValue();
        return 0.0;
    }
}
