this is simple viewpager indicator

use case is for example registration which has some step for validation.

simple unattributed state is like this

<img width="25%" height="100%" src="https://github.com/pirati02/CompletePagerIndicator/blob/master/app/src/main/res/drawable/a.png" />

and this is styled

<img width="25%" height="100%" src="https://github.com/pirati02/CompletePagerIndicator/blob/master/app/src/main/res/drawable/b.png"/>

       <com.dev.baqari.pagerindicator.CompleteIndicator
                android:id="@+id/complete_indicator"
                android:layout_width="match_parent"
                android:layout_height="50dp"
                app:currentItemRadius="45"
                app:filledItemColor="#7e00ec"
                app:radius="35"
                app:showLine="false"
                app:unFilledItemColor="#0059ff" />
        
