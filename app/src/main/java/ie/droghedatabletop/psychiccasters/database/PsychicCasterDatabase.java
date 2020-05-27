package ie.droghedatabletop.psychiccasters.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ie.droghedatabletop.psychiccasters.dao.CastersDao;
import ie.droghedatabletop.psychiccasters.entities.Caster;

@Database(entities = {Caster.class}, version = 3, exportSchema = false)
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
                            .addCallback(roomDatabaseCallback).addMigrations(new Migration[]{MIGRATION_1_2, MIGRATION_2_3}).build();
                }
            }
        }
        return INSTANCE;
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE caster "
                    + "ADD COLUMN powers TEXT");
        }
    };


    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Create the new table
            database.execSQL(
                    "CREATE TABLE caster_new (caster_name TEXT NOT NULL, powers TEXT, PRIMARY KEY(caster_name))");
            // Copy the data
            database.execSQL(
                    "INSERT INTO caster_new (caster_name, powers) SELECT caster_name, powers FROM caster");
                    // Remove the old table
                    database.execSQL("DROP TABLE caster");
            // Change the table name to the correct one
            database.execSQL("ALTER TABLE caster_new RENAME TO caster");
        }
    };
    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
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