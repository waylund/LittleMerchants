package com.brotherslynn.littlemerchants.objects;

/**
 * Created by danielmlynn on 10/14/17.
 */

public class Localization {

    private String localization = "eng";
    private String[] merchantType = new String[] {"Tools Merchant", "Food Merchant", "Soap Merchant"};
    private String[] merchantTypeDescriptions = new String[] {"The Tools Merchant trades common farming and industry tools.", "The Food Merchant trades foodstuffs like eggs, fish, milk, and bread.", "The Soap Merchant trades soap and other beauty supplies. A real favorite with the ladies."};
    private String greeting = "Hello ";

    public Localization()
    {

    }

    public Localization(String localization)
    {
        this.localization = localization.toLowerCase();
    }

    public String[] getMerchantType()
    {
        return merchantType;
    }

    public String[] getMerchantTypeDescriptions() {return merchantTypeDescriptions; }

    public String getGreeting() { return greeting; }
}
