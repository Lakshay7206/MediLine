package com.example.mediline.data.room

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [DepartmentEntity::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun departmentDao(): DepartmentDao
}
