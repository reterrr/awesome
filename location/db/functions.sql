CREATE OR REPLACE FUNCTION search_cities(
    search_name TEXT DEFAULT NULL,
    search_country TEXT DEFAULT NULL,
    search_lon REAL DEFAULT NULL,
    search_lat REAL DEFAULT NULL,
    proximity_threshold REAL DEFAULT 0.1
) RETURNS TABLE(id BIGINT, name TEXT, country TEXT, lon REAL, lat REAL) AS $$
BEGIN
    RETURN QUERY
        SELECT id, name, country, lon, lat
        FROM cities
        WHERE
            (search_name IS NULL OR name ILIKE '%' || search_name || '%') AND
            (search_country IS NULL OR country ILIKE '%' || search_country || '%') AND
            (
                search_lon IS NULL OR search_lat IS NULL OR
                (ABS(lon - search_lon) <= proximity_threshold AND ABS(lat - search_lat) <= proximity_threshold)
                )
        ORDER BY
            CASE WHEN search_lon IS NOT NULL AND search_lat IS NOT NULL THEN
                     (ABS(lon - search_lon) + ABS(lat - search_lat))
                 ELSE
                     NULL
                END ASC;
END;
$$ LANGUAGE plpgsql;
