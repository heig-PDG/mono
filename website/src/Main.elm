module Main exposing (main)

import Browser exposing (Document, UrlRequest(..))
import Html exposing (div, text)
import Json.Decode as D



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



--- UPDATE ---


type Msg
    = NoOp


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case ( msg, model ) of
        ( _, _ ) ->
            ( model, Cmd.none )



--- VIEW ---


view : Model -> Document Msg
view _ =
    let
        body =
            [ div
                []
                [ text "Empty for now" ]
            ]
    in
    { title = title, body = body }



-- SUBSCRIPTIONS ---


subscriptions : Model -> Sub Msg
subscriptions _ =
    Sub.none
