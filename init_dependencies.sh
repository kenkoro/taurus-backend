export PGPASSWORD=postgres
psql -U postgres> -d postgres << EOF
  create database taurus;
  \c taurus
  begin;
    create type order_status as enum ('Idle', 'Cut', 'Checked');
    create type user_profile as enum (
      'Admin',
      'Ceo',
      'Customer',
      'Manager',
      'Cutter',
      'Inspector',
      'Tailor',
      'Other'
    );
  commit;
EOF

exit 0