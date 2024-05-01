export PGPASSWORD=postgres
psql -U postgres> -d taurus << EOF
  begin;
  create type order_status as enum ('Idle', 'Cut', 'Checked');
  commit;
EOF

exit 0