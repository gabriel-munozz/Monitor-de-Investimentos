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

    //Consulta a API HG Brasil Finance e retorna um objeto CotacaoHistorica.
    public CotacaoHistorica buscarCotacao(String ticker) {

        String url = apiUrl + "?key=" + apiKey + "&symbol=" + ticker.toUpperCase();

        try {
            //Faz a requisição GET e converte o JSON em Map.
            Map resposta = restTemplate.getForObject(url, Map.class);

            if (resposta == null) {
                System.out.println("[API] Resposta vazia para: " + ticker);
                return null;
            }

            Map results = (Map) resposta.get("results");
            if (results == null) {
                System.out.println("[API] Campo 'results' ausente para: " + ticker);
                return null;
            }

            Map dadosAtivo = (Map) results.get(ticker.toUpperCase());
            if (dadosAtivo == null) {
                System.out.println("[API] Ticker não encontrado: " + ticker);
                return null;
            }

            // Preenche o objeto com os dados retornados pela API.
            CotacaoHistorica cotacao = new CotacaoHistorica();
            cotacao.setPreco(toDouble(dadosAtivo.get("price")));
            cotacao.setVariacaoPercentual(toDouble(dadosAtivo.get("change_percent")));
            cotacao.setPrecoMinimo(toDouble(dadosAtivo.get("low")));
            cotacao.setPrecoMaximo(toDouble(dadosAtivo.get("high")));
            cotacao.setConsultadoEm(LocalDateTime.now());

            System.out.println("[API] Cotação obtida: " + ticker + " = R$ " + cotacao.getPreco());
            return cotacao;

        //Erros
        } catch (ResourceAccessException e) {
            System.out.println("[API] Sem conexão com a API HG Brasil: " + e.getMessage());
            return null;

        } catch (HttpClientErrorException e) {
            System.out.println("[API] Erro do cliente (4xx): " + e.getStatusCode() + " - " + e.getMessage());
            return null;

        } catch (HttpServerErrorException e) {
            System.out.println("[API] Erro do servidor HG Brasil (5xx): " + e.getStatusCode() + " - " + e.getMessage());
            return null;

        } catch (Exception e) {
            System.out.println("[API] Erro inesperado ao consultar API: " + e.getMessage());
            return null;
        }
    }

    // Converte valores do JSON (Integer ou Double) para double.
    private double toDouble(Object valor) {
        if (valor == null) return 0.0;
        if (valor instanceof Number) return ((Number) valor).doubleValue();
        return 0.0;
    }
}
