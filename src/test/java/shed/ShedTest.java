package shed;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShedTest {
    static final String expectedValues = "Tool{code='CHNS', type='Chainsaw', brand='Stihl'}Tool{code='LADW', type='Ladder', brand='Werner'}Tool{code='JAKD', type='Jackhammer', brand='DeWalt'}Tool{code='JAKR', type='Jackhammer', brand='Ridgid'}";

    @Test
    void test_initialize_shed() {
        Shed expected = new Shed();
        Tool chns = new Tool();
        chns.setCode("CHNS");
        chns.setType("Chainsaw");
        chns.setBrand("Stihl");
        Tool ladw = new Tool();
        ladw.setCode("LADW");
        ladw.setType("Ladder");
        ladw.setBrand("Werner");
        Tool jakd = new Tool();
        jakd.setCode("JAKD");
        jakd.setType("Jackhammer");
        jakd.setBrand("DeWalt");
        Tool jakr = new Tool();
        jakr.setCode("JAKR");
        jakr.setType("Jackhammer");
        jakr.setBrand("Ridgid");
        expected.setShed(Arrays.asList(chns, ladw, jakd, jakr));
        Shed results = Shed.InitShed();
        assertEquals(expectedValues, results.toString(), String.format("expected: %s, actual: %s", expected, results));
    }
}