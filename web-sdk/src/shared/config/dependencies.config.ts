import { registerAs } from '@nestjs/config';

export default registerAs('dependencies', () => ({
  gateaway_url_with_port:
    process.env.GATEAWAY_URL_WITH_PORT,
}));
