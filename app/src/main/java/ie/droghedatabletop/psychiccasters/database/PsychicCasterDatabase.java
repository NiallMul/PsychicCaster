package ie.droghedatabletop.psychiccasters.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ie.droghedatabletop.psychiccasters.dao.CastersDao;
import ie.droghedatabletop.psychiccasters.entities.Caster;

@Database(entities = {Caster.class}, version = 1, exportSchema = false)
public abstract class PsychicCasterDatabase extends RoomDatabase {
    public abstract CastersDao dao();

    private static volatile PsychicCasterDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static PsychicCasterDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PsychicCasterDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PsychicCasterDatabase.class, "psychic_caster_database")
                            .addCallback(roomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }
    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                CastersDao dao = INSTANCE.dao();
                dao.deleteAll();

                Caster caster = new Caster("Balor (Warlord)", "smite(5),onslaught (7)");
                dao.insert(caster);
                caster = new Caster("Omna (Swords raised)", "smite(5),the horror (7)");
                dao.insert(caster);
                caster = new Caster("Bagna (Single sword raised)", "smite(5),catalyst (7)");
                dao.insert(caster);
                caster = new Caster("Maleceptor (Foot raised)", "smite(5),psychic scream (5)");
                dao.insert(caster);
                caster = new Caster("Maleceptor (all fours)", "smite(5),psychic scream (5)");
                dao.insert(caster);

            });
        }
    };
}