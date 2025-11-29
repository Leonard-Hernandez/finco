package com.finco.finco.infrastructure.account.controller;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.account.model.CurrencyEnum;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@Tag(name = "Account", description = "Account management endpoints")
public class GetCurrenciesController {

    @GetMapping("/accounts/currencies")
    @Operation(summary = "Get currencies", description = "Get currencies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Currencies found successfully", 
                content = @Content(schema = @Schema(implementation = CurrencyEnum[].class))) })
    @Tool(description = "Get currencies")
    public CurrencyEnum[] getCurrencies() {
        return CurrencyEnum.values();
    }

}
