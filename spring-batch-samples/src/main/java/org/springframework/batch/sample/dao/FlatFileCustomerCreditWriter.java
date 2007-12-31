/*
 * Copyright 2006-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.batch.sample.dao;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.ResourceLifecycle;
import org.springframework.batch.sample.domain.CustomerCredit;
import org.springframework.beans.factory.DisposableBean;

/**
 * Writes customer's credit information in a file.
 *
 * @see CustomerCreditWriter
 * @author Robert Kasanicky
 */
public class FlatFileCustomerCreditWriter implements CustomerCreditWriter,
		DisposableBean {

	private ItemWriter outputSource;

	private String separator = "\t";

	private volatile boolean opened = false;

	public void writeCredit(CustomerCredit customerCredit) {

		if (!opened) {
			open();
		}

		String line = "" + customerCredit.getName() + separator
				+ customerCredit.getCredit();

		outputSource.write(line);
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public void setOutputSource(ItemWriter outputSource) {
		this.outputSource = outputSource;
	}

	public void open() {
		if (outputSource instanceof ResourceLifecycle) {
			((ResourceLifecycle) outputSource).open();
		}
		opened = true;
	}

	public void close() {
		if (outputSource instanceof ResourceLifecycle) {
			((ResourceLifecycle) outputSource).close();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	public void destroy() throws Exception {
		close();
	}

	public void write(Object output) {
		writeCredit((CustomerCredit)output);
	}
}
