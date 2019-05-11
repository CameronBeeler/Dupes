package com.cambeeler;

public
class Card
{
    private String text;
    private boolean Swahili = false;

    public Card(String text, boolean swahili)
    {
        this.text = text;
        this.Swahili = swahili;
    }

    public
    String getText()
    {
        return text;
    }

    public
    boolean isSwahili()
    {
        return Swahili;
    }

    public String toString()
    {
        String output;
        if(this.Swahili)
        {
            output = "The swahili text is [" + this.text + "]\n";
        }
        else
        {
            output = "The english translation is [" + this.text + "]\n";
        }
        return output;
    }
}
