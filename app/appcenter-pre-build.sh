if [ -z "$APP_CENTER_BUILD"]
then
    echo "You need to define the APP_CENTER_BUILD in App Center"
    exit
fi

if [ -z "$APP_CENTER_API_KEY"]
then
    echo "You need to define the APP_CENTER_API_KEY in App Center"
    exit
fi

if [ -z "$API_KEY_HEADER_VALUE"]
then
    echo "You need to define the API_KEY_HEADER_VALUE in App Center"
    exit
fi