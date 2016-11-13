package es.maltimor.springUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.config.ListFactoryBean;

public class ListMerge extends ListFactoryBean {
	private final List<Object> listOfLists;
	
	public ListMerge(List<Object> listOfLists) throws Exception {
		this.listOfLists = listOfLists;
		setSourceList(new ArrayList<Object>());
	}

	@SuppressWarnings("unchecked")
	protected List<Object> createInstance() {
		List<Object> listOrigin = (List<Object>) super.createInstance();
		for (Iterator<Object> iter = listOfLists.iterator(); iter.hasNext();) {
			listOrigin.add(iter.next());
		}
		return listOrigin;
	}
}

