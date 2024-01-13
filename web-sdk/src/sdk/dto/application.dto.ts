export class ApplicationDto {
  id: number;
  name: string;
  email: string;
  url: string;
  description: string;
  apiKey: string;
  merchantId: number;

  constructor(
    id: number,
    name: string,
    email: string,
    url: string,
    description: string,
    apiKey: string,
    merchantId: number,
  ) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.url = url;
    this.description = description;
    this.apiKey = apiKey;
    this.merchantId = merchantId;
  }
}
