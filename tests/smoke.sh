#!/usr/bin/env bash
# ============================================================
# Prueba de humo de la API — CRUDL de carreras de taxi (ejercicio 19)
#
# Recorre el flujo completo contra una instancia en ejecución:
# health -> login -> listar -> registrar -> consultar -> actualizar
# -> eliminar, más reglas de dominio, autorización por rol y el
# rechazo de una cuenta no activa.
#
# Uso:
#   BASE_URL=http://localhost:8080 bash tests/smoke.sh
#
# Requiere curl y jq, y la base creada con src/main/resources/schema.sql.
# ============================================================
set -u

BASE_URL="${BASE_URL:-http://localhost:8080}"
failures=0

check() {
  # check "descripción" "esperado" "obtenido"
  if [ "$2" = "$3" ]; then
    echo "  OK    $1"
  else
    echo "  FALLA $1 (esperado: $2, obtenido: $3)"
    failures=$((failures + 1))
  fi
}

# request MÉTODO RUTA [TOKEN] [CUERPO_JSON] -> deja el cuerpo en $BODY y el código en $CODE
request() {
  local method="$1" path="$2" token="${3:-}" body="${4:-}"
  local args=(-sS -o /tmp/smoke-body -w "%{http_code}" -X "$method" "$BASE_URL$path"
    -H "Content-Type: application/json")
  [ -n "$token" ] && args+=(-H "Authorization: Bearer $token")
  [ -n "$body" ] && args+=(--data "$body")
  CODE="$(curl "${args[@]}")"
  BODY="$(cat /tmp/smoke-body)"
}

login() {
  request POST /api/auth/login "" "{\"email\":\"$1\",\"password\":\"$2\"}"
  printf '%s' "$BODY" | jq -r '.accessToken // empty'
}

carrera_json() {
  # carrera_json PLACA PASAJEROS PRECIO
  cat <<JSON
{"cliente":"Cliente Prueba Humo","taxi":"$1","kilometros":4.5,"barrioInicio":"Torices",
 "barrioLlegada":"Getsemaní","cantidadPasajeros":$2,"taxista":"Taxista Prueba",
 "precio":$3,"duracionMinutos":15}
JSON
}

echo "Prueba de humo contra $BASE_URL"

request GET /health
check "GET /health responde 200" 200 "$CODE"

request GET /api/carreras-taxi
check "las carreras exigen token (401)" 401 "$CODE"

ADMIN_TOKEN="$(login admin@example.com 'Admin1234!')"
check "login del administrador entrega un token" yes "$([ -n "$ADMIN_TOKEN" ] && echo yes || echo no)"

MEMBER_TOKEN="$(login member@example.com 'Member1234!')"
REVIEWER_TOKEN="$(login reviewer@example.com 'Reviewer1234!')"
check "login de MEMBER y REVIEWER" yes \
  "$([ -n "$MEMBER_TOKEN" ] && [ -n "$REVIEWER_TOKEN" ] && echo yes || echo no)"

login pending@example.com 'Pending1234!' > /dev/null
check "una cuenta PENDING no puede iniciar sesión (401)" 401 "$CODE"

request GET /api/carreras-taxi "$REVIEWER_TOKEN"
check "L: listar carreras (200)" 200 "$CODE"
check "L: el listado trae las carreras de ejemplo" yes \
  "$([ "$(printf '%s' "$BODY" | jq 'length')" -ge 3 ] && echo yes || echo no)"

request POST /api/carreras-taxi "$MEMBER_TOKEN" "$(carrera_json xyz-987 3 10000)"
check "C: registrar carrera como MEMBER (201)" 201 "$CODE"
ID="$(printf '%s' "$BODY" | jq -r '.id')"
check "C: la placa se normaliza a XYZ987" XYZ987 "$(printf '%s' "$BODY" | jq -r '.taxi')"

request GET "/api/carreras-taxi/$ID" "$REVIEWER_TOKEN"
check "R: consultar carrera (200)" 200 "$CODE"
VELOCIDAD="$(printf '%s' "$BODY" | jq -r '.velocidadPromedioKmH')"
check "R: velocidad promedio calculada por el dominio (4.5 km en 15 min)" 18.00 \
  "$(awk -v v="$VELOCIDAD" 'BEGIN { printf "%.2f", v }')"

request PUT "/api/carreras-taxi/$ID" "$ADMIN_TOKEN" "$(carrera_json XYZ987 4 12500)"
check "U: actualizar carrera (200)" 200 "$CODE"
check "U: el precio quedó actualizado" 12500 "$(printf '%s' "$BODY" | jq -r '.precio | floor')"

request POST /api/carreras-taxi "$ADMIN_TOKEN" "$(carrera_json 12AB 2 9000)"
check "regla de dominio: placa inválida (400)" 400 "$CODE"

request POST /api/carreras-taxi "$ADMIN_TOKEN" "$(carrera_json ABC123 9 9000)"
check "regla de dominio: más de 4 pasajeros (400)" 400 "$CODE"

request POST /api/carreras-taxi "$REVIEWER_TOKEN" "$(carrera_json ABC123 2 9000)"
check "autorización: REVIEWER no registra carreras (403)" 403 "$CODE"

request DELETE "/api/carreras-taxi/$ID" "$MEMBER_TOKEN"
check "autorización: MEMBER no elimina carreras (403)" 403 "$CODE"

request DELETE "/api/carreras-taxi/$ID" "$ADMIN_TOKEN"
check "D: eliminar carrera como ADMIN (204)" 204 "$CODE"

request GET "/api/carreras-taxi/$ID" "$ADMIN_TOKEN"
check "D: la carrera eliminada ya no existe (404)" 404 "$CODE"

echo
if [ "$failures" -gt 0 ]; then
  echo "Resultado: $failures verificación(es) fallaron."
  exit 1
fi
echo "Resultado: todas las verificaciones pasaron."
