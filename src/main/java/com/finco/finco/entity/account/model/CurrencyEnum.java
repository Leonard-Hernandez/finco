package com.finco.finco.entity.account.model;

import lombok.Getter;

@Getter
public enum CurrencyEnum {
    // Major Fiat Currencies
    USD("US Dollar"),
    EUR("Euro"),
    GBP("British Pound"),
    JPY("Japanese Yen"),
    AUD("Australian Dollar"),
    CAD("Canadian Dollar"),
    CHF("Swiss Franc"),
    CNY("Chinese Yuan"),
    
    // Latin American Currencies
    ARS("Argentine Peso"),
    BOB("Bolivian Bolíviano"),
    BRL("Brazilian Real"),
    CLP("Chilean Peso"),
    COP("Colombian Peso"),
    DOP("Dominican Peso"),
    GTQ("Guatemalan Quetzal"),
    HNL("Honduran Lempira"),
    MXN("Mexican Peso"),
    NIO("Nicaraguan Córdoba"),
    PAB("Panamanian Balboa"),
    PEN("Peruvian Sol"),
    PYG("Paraguayan Guarani"),
    UYU("Uruguayan Peso"),
    VES("Venezuelan Bolívar"),
    
    // Cryptocurrencies
    BTC("Bitcoin"),
    ETH("Ethereum"),
    USDT("Tether"),
    BNB("Binance Coin"),
    USDC("USD Coin"),
    XRP("Ripple"),
    ADA("Cardano"),
    DOGE("Dogecoin"),
    SOL("Solana"),
    DOT("Polkadot"),
    SHIB("Shiba Inu"),
    AVAX("Avalanche"),
    MATIC("Polygon"),
    LTC("Litecoin"),
    UNI("Uniswap"),
    LINK("Chainlink"),
    XLM("Stellar"),
    ATOM("Cosmos"),
    XMR("Monero"),
    ETC("Ethereum Classic"),
    FIL("Filecoin");

    private final String displayName;

    CurrencyEnum(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return this.name() + " - " + displayName;
    }
}
