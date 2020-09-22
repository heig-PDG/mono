module Main exposing (main)

import Browser exposing (Document, UrlRequest(..))
import Browser.Navigation exposing (Key)
import Html exposing (Html, div, h1, text)
import Html.Attributes exposing (class)
import Json.Decode as D
import Url exposing (Url)



--- MAIN ---


main : Program D.Value Model Msg
main =
    Browser.application
        { init = init
        , view = view
        , update = update
        , subscriptions = subscriptions
        , onUrlRequest = onUrlRequest
        , onUrlChange = onUrlChange
        }



--- MODEL ---


type Model
    = Empty


init : D.Value -> Url -> Key -> ( Model, Cmd Msg )
init _ _ _ =
    ( Empty, Cmd.none )


onUrlRequest : UrlRequest -> Msg
onUrlRequest urlRequest =
    ClickedLink urlRequest


onUrlChange : Url -> Msg
onUrlChange url =
    ChangedUrl url



--- UPDATE ---


type Msg
    = ClickedLink UrlRequest
    | ChangedUrl Url


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case ( msg, model ) of
        ( _, _ ) ->
            ( model, Cmd.none )



--- VIEW ---


view : Model -> Document Msg
view _ =
    let
        title =
            "Tupperdate.me"

        body =
            [ viewLanding
            ]
    in
    { title = title, body = body }


viewLanding : Html Msg
viewLanding =
    div
        [ class "w-full h-screen"
        ]
        [ h1
            [ class "text-6xl font-bold"
            , class "customTitleGradient"
            ]
            [ text "Tupperdate" ]
        ]



-- SUBSCRIPTIONS ---


subscriptions : Model -> Sub Msg
subscriptions _ =
    Sub.none
