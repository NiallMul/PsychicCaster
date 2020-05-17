package ie.droghedatabletop.psychiccasters.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import ie.droghedatabletop.psychiccasters.dao.CastersDao;
import ie.droghedatabletop.psychiccasters.database.PsychicCasterDatabase;
import ie.droghedatabletop.psychiccasters.entities.Caster;

public class CasterRepository {
    private CastersDao castersDao;
    private LiveData<List<Caster>> listData;

    public CasterRepository(Application application) {
        PsychicCasterDatabase db = PsychicCasterDatabase.getDatabase(application.getApplicationContext());
        castersDao = db.dao();
        listData = castersDao.getCasters();
    }

    public LiveData<List<Caster>> getAllCasters() {
        return listData;
    }

    public void insert(Caster caster) {
        PsychicCasterDatabase.databaseWriteExecutor.execute(() -> castersDao.insert(caster));
    }
}
