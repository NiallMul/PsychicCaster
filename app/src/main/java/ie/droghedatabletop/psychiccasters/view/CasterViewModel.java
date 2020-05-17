package ie.droghedatabletop.psychiccasters.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ie.droghedatabletop.psychiccasters.entities.Caster;
import ie.droghedatabletop.psychiccasters.repository.CasterRepository;

public class CasterViewModel extends AndroidViewModel {
    private CasterRepository repository;
    private LiveData<List<Caster>> casters;

    public CasterViewModel(@NonNull Application application) {
        super(application);
        repository = new CasterRepository(application);
        casters = repository.getAllCasters();
    }

    public LiveData<List<Caster>> getCasters() {
        return casters;
    }

    public void insert(Caster caster) {
        repository.insert(caster);
    }
}
