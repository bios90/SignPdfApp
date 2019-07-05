package com.dimfcompany.signpdfapp.local_db.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.dimfcompany.signpdfapp.models.Model_Color;
import com.dimfcompany.signpdfapp.models.Model_Control;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.models.Model_Krep;
import com.dimfcompany.signpdfapp.models.Model_Material;
import com.dimfcompany.signpdfapp.models.Model_Price_Element;
import com.dimfcompany.signpdfapp.models.Model_Product;
import com.dimfcompany.signpdfapp.models.Model_Vaucher;

@Database(entities = {Model_Document.class, Model_Product.class,Model_Color.class, Model_Control.class, Model_Krep.class, Model_Material.class, Model_Vaucher.class, Model_Price_Element.class},version = 9)
public abstract class AppDatabase extends RoomDatabase
{
    public abstract DaoDocument getDaoDocument();
    public abstract DaoProduct getDaoProduct();

    public abstract DaoColor getDaoColor();
    public abstract DaoControl getDaoControl();
    public abstract DaoKrep getDaoKrep();
    public abstract DaoMaterial getDaoMaterial();

    public abstract DaoVaucher getDaoVaucher();
    public abstract DaoPriceElements getDaoPriceElements();
}
