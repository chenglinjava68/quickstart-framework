/*
 * Copyright 2015 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.quickstart.reactivex.rxnetty.examples.tcp.streaming;

import org.junit.Test;
import org.quickstart.reactivex.rxnetty.examples.ExamplesTestUtil;
import org.quickstart.reactivex.rxnetty.examples.tcp.streaming.StreamingClient;

import java.util.Queue;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class StreamingTest {

    @Test(timeout = 60000)
    public void testStreaming() throws Exception {

        Queue<String> output = ExamplesTestUtil.runClientInMockedEnvironment(StreamingClient.class);

        assertThat("Unexpected number of messages echoed", output, hasSize(10));

        String[] content = new String[10];
        for (int i = 0; i < 10; i++) {
            content[i] = "Interval =>" + i;
        }

        assertThat("Unexpected content", output, contains(content));
    }
}