package com.cambeeler;

import java.util.ArrayList;
import java.util.List;

public
class CardStack
{
    private List<Card> stack;

    @Override
    public
    String toString()
    {
        String output="";
        for (int i=0;i<stack.size();i++)
        {
            if(stack.get(i).isSwahili())
            {
                output += " Card 1, Swahili is = " + stack.get(i).getText() + "\n";
            }
            else
            {
                output += "Card " + (i+1) + ", English is = " + stack.get(i).getText() + "\n";
            }
        }
        return output;
    }

    public
    CardStack()
    {
        System.out.println("CardStack Constructor");
        this.stack = new ArrayList<Card>();
    }

    public boolean addCard(String cardText, boolean swahili)
    {
        return stack.add(new Card(cardText, swahili));

    }


}
