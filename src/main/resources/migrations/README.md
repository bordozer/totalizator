Database Migrations
===================

Database migration/upgrade scripts are kept in `src/main/resources/migrations` directory. 

In order to keep migrations order relevant it is helpful to keep proper file naming. Current names are based on date when file creation date. Date format is as follows: `YYYYMMDDHHmmss_<migration-name>.sql`

There is a helper script for creating migrations. Use `create-migration.sh <migration-name>` to make empty migration file following naming convention. Than put your SQL there.
