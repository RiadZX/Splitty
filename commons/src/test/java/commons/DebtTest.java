/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DebtTest {

	@Test
	public void checkConstructor() {
		var p = new Debt(new Expense(), new Participant(), 0);
		assertEquals(0, p.get_amount());
	}

	@Test
	public void equalsHashCode() {
		var a = new Debt(new Expense(), new Participant(), 0);
		var b = new Debt(new Expense(), new Participant(), 0);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void notEqualsHashCode() {
		var a = new Debt(new Expense(), new Participant(), 0);
		var b = new Debt(new Expense(), new Participant(), 1);
		assertNotEquals(a, b);
		assertNotEquals(a.hashCode(), b.hashCode());
	}
}
