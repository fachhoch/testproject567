package org.seva.dc.ns.doa;

import org.seva.dc.ns.domain.Ns;
import org.springframework.stereotype.Repository;

@Repository(value="nsDAO")
public class JpaNsDAO extends GenericDaoJpaImpl<Ns, Long> implements NsDAO {
	
	
}
