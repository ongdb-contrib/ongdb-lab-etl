# ONgDB-LAB-ETL
- MAIN PROTOCOL:BOLT ROUTING

## CORE INTERFACE
### SERVER MANAGE
>CORE AND READ_REPLICA
>DEV - TEST
>PRO - FORMAL
data.lab.ongdb.etl.properties.ServerConfiguration

### COMPOSER
data.lab.ongdb.etl.compose
- 1.Support batch dynamic update imports
- 2.Load csv(HTTP SERVICE LOAD FILE)
- 3.Batch merge
- 4.Shell run

### INDEXER
data.lab.ongdb.etl.index
- node/relationship/path indices interface

### UPDATER
data.lab.ongdb.etl.update
- node/relationship/path update interface - No CYPHER restrictions, it can support a large number of delete operations


