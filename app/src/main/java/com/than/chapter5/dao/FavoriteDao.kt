package com.than.chapter5.dao

import androidx.room.*
import com.than.chapter5.model.Favorite

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite WHERE id_user= :id_user AND country_name = :country_name")
    fun cekFavorite(id_user: Int, country_name: String): Boolean
    @Query("SELECT * FROM favorite WHERE id_user = :id_user")
    fun getFavorite(id_user: Int): List<Favorite>
    @Query("SELECT * FROM favorite WHERE id_user= :id_user AND country_name = :country_name")
    fun getFavoriteByIdAndCountry(id_user: Int, country_name: String): Favorite
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFavorite(favorite: Favorite):Long
    @Delete
    fun deleteFavorite(favorite: Favorite): Int
}