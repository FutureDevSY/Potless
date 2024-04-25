import 'dart:convert';

import 'package:porthole24/models/search_results.dart';
import 'package:http/http.dart' as http;

Future<List<SearchResultItem>> fetchSearchResults(String keyword) async {
  var url = Uri.parse(
    'https://dapi.kakao.com/v2/local/search/keyword.json?query=$keyword',
  );

  var response = await http.get(
    url,
    headers: {'Authorization': 'KakaoAK 7c174eea70a4891533065fac4cedaca2'},
  );

  if (response.statusCode == 200) {
    List<dynamic> jsonList = json.decode(response.body)['documents'];
    List<SearchResultItem> items = jsonList
        .map((jsonItem) => SearchResultItem.fromJson(jsonItem))
        .toList();
    return items;
  } else {
    throw Exception('Failed to load search results');
  }
}
