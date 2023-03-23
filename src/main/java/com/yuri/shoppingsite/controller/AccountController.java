package com.yuri.shoppingsite.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AccountController {

        @GetMapping("/account/auth")
        public String getAccountAuth(Model model) {
            return "account/auth";
        }

        @PostMapping("/account/auth")
        public String postAccountAuth(Model model, @RequestParam String bankCode, @RequestParam String accountNumber, HttpSession session) throws IOException {
            OkHttpClient client = new OkHttpClient();

            String accessToken = (String) session.getAttribute("accessToken");

            if (accessToken == null) {
                return "redirect:/oauth/authorize";
            }

            String url = "https://testapi.openbanking.or.kr/v2.0/inquiry/real_name";
            MediaType mediaType = MediaType.parse("application/json");

            Map<String, String> params = new HashMap<>();
            params.put("bank_tran_id", "T991671840U" + System.currentTimeMillis());
            params.put("cntr_account_type", "N");
            params.put("cntr_account_num", "01079493417");
            params.put("dps_print_content", "쇼핑몰환불");
            params.put("fintech_use_num", "199167184057870000000000");
            params.put("tran_dtime", "20211231154801");

            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(params);

            RequestBody body = RequestBody.create(requestBody, mediaType);

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("charset", "UTF-8")
                    .build();

            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String responseBody = response.body().string();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            String name = jsonNode.get("user_name").asText();

            model.addAttribute("name", name);

            return "account/result";
        }
    }

