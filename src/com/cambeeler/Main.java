package com.cambeeler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public
class Main
{
    private String FNAME = "/Users/cam/Downloads/Multiple stacks.csv";
    private List<CardStack> deck = new ArrayList<>();

    public static
    void main(String[] args)
    {
        Main m = new Main();
        File f = new File(FNAME);

        try
        {
            String contents = new String(Files.readAllBytes(Paths.get(FNAME)), StandardCharsets.UTF_16);
            String QTEOL    = "\"\n";
            String EOLQT    = "\n\"";
            String QT       = "\"";
            String EOL      = "\n";
            String EOLComma = "\n,";

            String       tempInfo        = "";
            List<String> tempCards       = new ArrayList<>();
            int          index           = 0;
            int          maxLength       = contents.length();
            boolean      EOF             = false;
            boolean      ENDOFRECORD     = false;
            char         CharAtNextIndex = ' ';
            int          stackCount       = 0;
            int          cardCount       = 0;
            int          cardInStack     = 0;

            while (!EOF && index > -1) // to identify the stack and submit the stack to
            {

                cardInStack++;
                if (cardInStack == 1)
                {
                    CardStack<Card> stack = new CardStack();
                    m.deck.add(stack);
                    tempCards.clear();
                } // begin with a clear stack added to deck, and ready for adding cards

                if (contents.charAt(index) == '"') // if the character is a quote, then search for the next quote
                {
                    int nextQuoteIndex = contents.indexOf(index + 1, '"');
                    tempInfo = contents.substring(index, nextQuoteIndex);
                    tempInfo = tempInfo.replaceAll("\n", ",");
                    m.deck.get(stackCount).addCard(tempInfo, cardInStack==1?true:false);

                    if (cardInStack == 1)
                    {
                        index = contents.indexOf(nextQuoteIndex, ',');
                        if (index > -1) index++; // gets me to the next record!
                    }
                    else // card 2+
                    {
                        if(nextQuoteIndex+2 == maxLength) // end of file
                        {
                            EOF = true;
                            System.out.println("EOF - finished loading!");
                            break;
                        } // if not EOF, then are we at the next card?
                        else if(contents.charAt(nextQuoteIndex+1)==',') // this mean another
                        {
                            index = contents.indexOf(nextQuoteIndex, ',');
                        }
                        else if (contents.substring(nextQuoteIndex+1, nextQuoteIndex+3) == "\n,")
                        {
                            index = contents.indexOf(nextQuoteIndex, ',');

                        }
                        else // the beginning of a new cardstack
                        {
                            System.out.println(cardInStack + " cards in this stack");
                            index = nextQuoteIndex+2;
                            cardInStack = 0;
                        }
                    }
                }
                else // simple case - no quotes...
                {
                    int nextComma = contents.indexOf(index, ',');
                    int nextEOL   = contents.indexOf(index, '\n');
                    if(cardInStack==1) // this is the first in the stack, starts at the beginning of line
                    {
                        tempInfo = contents.substring(index, nextComma);

                    }
                    else // if card is 2+, then you must check for EOL/EOF & next comma - card
                    {
                        if(nextEOL+1 < maxLength) // EOF test!
                        {
                            if(nextEOL<nextComma) // get the card and add it
                            {
                                tempInfo = contents.substring(index, nextEOL);
                                index=nextEOL+1;
                            }
                            else
                            {
                                tempInfo = contents.substring(index, nextComma);
                                index=nextComma+1;
                            }
                            m.deck.get(stackCount).addCard(tempInfo, cardInStack==1?true:false);
                        }
                        else
                        {
                            EOF = true;
                            System.out.println("EOF - finished loading!");
                            System.out.println((cardCount+1) + " cards stored in stacks);
                            break;
                        }
                    }
                }
                cardCount++;
            }

            System.out.println("There were a total of " + cardCount + " cards");
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }


    public static
    void parseNextCell(List<CardStack> deck, String line)
    {
        System.out.println("parseNextCell");
//               System.out.println(fileContent);
        CardStack node      = new CardStack();
        int       index     = 0;
        String    result    = "";
        int       increment = 0;
        boolean   proceed   = false;
        do
        {
            proceed = true;
            if (line.charAt(index) == '"')
            { //if " text, text" then just extract the entire field between the quotes...
//      njooni,"come, all of you"
                System.out.println("begin quote section");
                index++; // move one position beyond the " mark to find the next "'s mark.

                result = line.substring(index, line.indexOf('"', index));
                int endCell = line.indexOf('"', index); // obtain the end of the quoted string
                endCell += 2;
                if (endCell > line.length())
                {
                    proceed = false;// we are at the end of the string...done.
                }
                else
                {
                    index = endCell; // This clears the next
                    System.out.println("quotes - cell =>" + result + "\nendCell==>" + endCell);
                }
                System.out.println(result + " " + proceed);
            }
            else
            {   // begin with a starting index and an ending index, then get the line...
                System.out.println("begin non quote section " + line.indexOf(',', index));
                if (line.indexOf(',', index) == -1)
                {
                    System.out.println("-1");
                    result = line.substring(index, line.length());
                    proceed = false; // this means we are on the outside value
                    System.out.println("comma - cell =>" + result);
                }
                else
                {
                    System.out.println("+");
                    result = line.substring(index, line.indexOf(',', index));
                    int endCell = line.indexOf(',', index);
                    System.out.println("endCell = " + endCell);
                    index = endCell + 1; // operating with the assumption that the , suggests another field
                    System.out.println("index = " + index);
                    System.out.println("comma - cell =>" + result + "\nendCell ==>" + endCell);
                }
            }
            System.out.println(index + " : " + line.length());
            if (!result.isBlank())
            {
                node.addCard(result, false); // need to track - 1st card is swahili...true
            }
            if (increment++ > 3)
            {
                break;
            }
        } while (proceed);
        deck.add(node);
    }
}
