import cnchar from 'cnchar';
import name from 'cnchar-name';
import radical from 'cnchar-radical';
cnchar.use(name); // use 在浏览器环境中非必须
cnchar.use(radical); // use 在浏览器环境中非必须

export async function onRequestPost(context) {
  const { cchar } = await context.request.json();
  return Response.json({"stroke": cnchar.stroke(cchar)});
}



