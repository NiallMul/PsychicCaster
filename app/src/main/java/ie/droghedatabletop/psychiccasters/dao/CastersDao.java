package ie.droghedatabletop.psychiccasters.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ie.droghedatabletop.psychiccasters.entities.Caster;

@Dao
public interface CastersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Caster caster);

    @Query("Delete from caster")
    void deleteAll();

    @Query("Select * from caster")
    LiveData<List<Caster>> getCasters();
}
