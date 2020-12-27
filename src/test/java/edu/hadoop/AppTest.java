package edu.hadoop;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void splitString(){
        String str = "1::Toy Story (1995)::Animation|Children's|Comedy";
        String[] splits = str.split("::");
        System.out.println(splits[0]+"  "+splits[1]+"   "+splits[2]);
    }
}
