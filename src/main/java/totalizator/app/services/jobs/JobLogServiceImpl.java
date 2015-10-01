package totalizator.app.services.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.dao.JobLogDao;
import totalizator.app.models.JobLog;

import java.util.List;

@Service
public class JobLogServiceImpl implements JobLogService {

	@Autowired
	private JobLogDao jobLogRepository;

	@Override
	@Transactional( readOnly = true )
	public List<JobLog> loadAll() {
		return jobLogRepository.loadAll();
	}

	@Override
	@Transactional( readOnly = true )
	public JobLog load( final int id ) {
		return jobLogRepository.load( id );
	}

	@Override
	@Transactional
	public JobLog save( final JobLog entry ) {
		return jobLogRepository.save( entry );
	}

	@Override
	@Transactional
	public void delete( final int id ) {
		jobLogRepository.delete( id );
	}

	@Override
	@Transactional( readOnly = true )
	public List<JobLog> loadAll( final int jobTaskId ) {
		return jobLogRepository.loadAll( jobTaskId );
	}
}
