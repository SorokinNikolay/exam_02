# language: ru

@Api
Функция: Работа с API

  @mortyTest
  Сценарий: Сравнение Морти с последним персонажем из последней серии, где был Морти
    Когда получаем информацию о Морти
    Тогда узнаём последний эпизод, в котором был Морти и запрашиваем информацию о нём
    И узнаём последнего персонажа из эпизода и запрашиваем о нём информацию
    И сраниваем рассы и локации Морти и последнего персонажа

    @potatoTest
    Сценарий: Отправка запроса, получение ответа и сравнение результата
      Когда достаём JSONObject из файла test2.json
      Тогда назначаем в JSONObject нужные по условиям значения по ключам name и job
      И отправляем запрос на reqres и парсим ответ
      И проверяем, содержит ли вернувшийся JSONObject нужные значения по ключам