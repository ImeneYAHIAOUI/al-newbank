import { registerAs } from '@nestjs/config';

export default registerAs('dependencies', () => ({
  load_balancer_url:
    process.env.LOAD_BALANCER_URL,
}));
