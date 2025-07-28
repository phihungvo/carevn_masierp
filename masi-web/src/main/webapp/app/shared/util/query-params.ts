export type QueryParamsType = Record<string | number, any>;

export function encodeQueryParam(key: string, value: any) {
  const encodedKey = encodeURIComponent(key);
  return `${encodedKey}=${encodeURIComponent(typeof value === 'number' ? value : `${value}`)}`;
}

export function addQueryParam(query: QueryParamsType, key: string) {
  return encodeQueryParam(key, query[key]);
}

export function addArrayQueryParam(query: QueryParamsType, key: string) {
  const value = query[key];
  return value.map((v: any) => this.encodeQueryParam(key, v)).join('&');
}

export function toQueryString(rawQuery?: QueryParamsType): string {
  const query = rawQuery || {};
  const keys = Object.keys(query).filter(key => 'undefined' !== typeof query[key]);
  return keys.map(key => (Array.isArray(query[key]) ? this.addArrayQueryParam(query, key) : this.addQueryParam(query, key))).join('&');
}

export function addQueryParams(rawQuery?: QueryParamsType): string {
  const queryString = this.toQueryString(rawQuery);
  return queryString ? `?${queryString}` : '';
}
