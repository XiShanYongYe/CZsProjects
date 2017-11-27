package org.nust.wsong.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.nust.wsong.algorithm.Algorithm1;
import org.nust.wsong.model.Trace;

public class RecoveryTest {
	@Test
	public void testName() throws Exception {
		Trace dtrace = new Trace();
		dtrace.append("t1");
		dtrace.append("t3");
/*		dtrace.append("t2");
		dtrace.append("t4");
		dtrace.append("t6");
		dtrace.append("t8");
		dtrace.append("t9");*/
		
		Trace t1 = new Trace();
		Collections.addAll(t1.getEvents(), "t1","t2","t3","t4","t5","t6","t7","t8");
		
		Trace t2 = new Trace();
		Collections.addAll(t2.getEvents(), "t1","t4","t5","t2","t3","t6","t9");
		
		Trace t3 = new Trace();
		Collections.addAll(t3.getEvents(), "t1","t4","t2","t5","t3","t6","t9");
		
		List<Trace> traces = new ArrayList<>();
		traces.add(t1);
		traces.add(t2);
		traces.add(t3);
		
		Algorithm1.traceCluster(traces);
		
		Algorithm1.recovery(dtrace);
		
		
	}
}
