export PGPASSWORD=postgres
psql -U postgres> -d taurus << EOF
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