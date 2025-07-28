DO
$$
-- declare
    BEGIN


        ALTER table customer
            add column if not exists contract_from date;
        ALTER table customer
            add column if not exists contract_to date;

/* pl/pgsql here */
    END
$$;
