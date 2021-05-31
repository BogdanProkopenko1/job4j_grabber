import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class AssemblyTest {

    @Test
    public void assembly() {
        assertThat(new Assembly().assembly(), is(1));
    }
}