export interface IApiError {
  type: string;
  status: number;
  detail: string;
  instance: string;
  message: string;
}
