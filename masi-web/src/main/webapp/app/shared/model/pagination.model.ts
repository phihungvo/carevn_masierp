export interface PaginationParams {
  page?: number;
  size?: number;
}

export interface PaginationResponse<T> {
  data: T[];
  totalRecord: number;
}

export interface BaseOption {
  label: string;
  value: string | number;
}
