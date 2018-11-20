package org.quickstart.reactivex.rxnetty.examples.local;

import org.junit.Ignore;
import org.junit.Test;
import org.quickstart.reactivex.rxnetty.examples.ExamplesTestUtil;
import org.quickstart.reactivex.rxnetty.examples.local.LocalEcho;

import java.util.Queue;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@Ignore("travis doesn't like me")
public class LocalEchoTest {

    @Test(timeout = 60000)
    public void testEcho() throws Exception {
        Queue<String> output = ExamplesTestUtil.runClientInMockedEnvironment(LocalEcho.class);

        assertThat("Unexpected number of messages echoed", output, hasSize(1));
        assertThat("Unexpected number of messages echoed", output, contains("echo => Hello World!"));
    }
}
