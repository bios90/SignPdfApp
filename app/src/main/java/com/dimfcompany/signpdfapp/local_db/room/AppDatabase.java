package com.dimfcompany.signpdfapp.local_db.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.dimfcompany.signpdfapp.models.Model_Color;
import com.dimfcompany.signpdfapp.models.Model_Control;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.models.Model_Krep;
import com.dimfcompany.signpdfapp.models.Model_Material;
import com.dimfcompany.signpdfapp.models.Model_Product;

@Database(entities = {Model_Document.class, Model_Product.class,Model_Color.class, Model_Control.class, Model_Krep.class, Model_Material.class},version = 2)
public abstract class AppDatabase extends RoomDatabase
{
    public abstract DaoDocument getDaoDocument();
    public abstract DaoProduct getDaoProduct();

    public abstract DaoColor getDaoColor();
    public abstract DaoControl getDaoControl();
    public abstract DaoKrep getDaoKrep();
    public abstract DaoMaterial getDaoMaterial();
}
